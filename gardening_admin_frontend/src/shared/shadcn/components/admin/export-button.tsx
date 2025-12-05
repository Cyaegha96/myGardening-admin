import * as React from "react";
import { useCallback } from "react";
import { Download } from "lucide-react";
import type { Exporter } from "ra-core";
import {
  fetchRelatedRecords,
  useDataProvider,
  useNotify,
  useListContext,
  Translate,
} from "ra-core";
import { Button } from "@/shared/shadcn/components/ui/button";


import { unparse } from "papaparse";

const csvExporter: Exporter = (records, _fetchRelatedRecords, _dataProvider, resource) => {
    const csv = unparse(records, { delimiter: "," });
    const blob = new Blob(
        ["\uFEFF" + csv], // BOM 추가
        { type: "text/csv;charset=utf-8;" }
    );
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `${resource}.csv`;
    a.click();
};



export const ExportButton = (props: ExportButtonProps) => {
  const {
    maxResults = 1000,
    onClick,
    label = "ra.action.export",
    icon = defaultIcon,

    meta,
    className = "cursor-pointer",
  } = props;
  const {
    filter,
    filterValues,
    resource,
    sort,
    exporter: exporterFromContext,
    total,
  } = useListContext();
  const exporter = csvExporter || exporterFromContext;
  const dataProvider = useDataProvider();
  const notify = useNotify();
  const handleClick = useCallback(
    (event: React.MouseEvent<HTMLButtonElement>) => {
      dataProvider
        .getList(resource, {
          sort,
          filter: filter ? { ...filterValues, ...filter } : filterValues,
          pagination: { page: 1, perPage: maxResults },
          meta,
        })
        .then(
          ({ data }) =>
            exporter &&
            exporter(
              data,
              fetchRelatedRecords(dataProvider),
              dataProvider,
              resource,
            ),
        )
        .catch((error) => {
          console.error(error);
          notify("HTTP Error", { type: "error" });
        });
      if (typeof onClick === "function") {
        onClick(event);
      }
    },
    [
      dataProvider,
      exporter,
      filter,
      filterValues,
      maxResults,
      notify,
      onClick,
      resource,
      sort,
      meta,
    ],
  );

  return (
    <Button
      variant="outline"
      onClick={handleClick}
      disabled={total === 0}
      className={className}
    >
      {icon}
      <Translate i18nKey={label}>Export</Translate>
    </Button>
  );
};

const defaultIcon = <Download />;

export interface ExportButtonProps {
  className?: string;
  exporter?: Exporter;
  icon?: React.ReactNode;
  label?: string;
  maxResults?: number;
  onClick?: (e: React.MouseEvent<HTMLButtonElement>) => void;
  resource?: string;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  meta?: any;
}
