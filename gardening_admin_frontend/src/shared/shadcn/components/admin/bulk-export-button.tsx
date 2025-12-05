import * as React from "react";
import { Download } from "lucide-react";
import type { RaRecord, Exporter } from "ra-core";
import { Translate, useBulkExport, type UseBulkExportOptions } from "ra-core";
import { Button } from "../ui/button";

import { unparse } from "papaparse";

/* -------------------------------------------------
 *  CSV EXPORTER — 한글 인코딩(BOM 포함) 버전
 * ------------------------------------------------- */
const csvExporter: Exporter = (records, _fetchRelated, _dataProvider, resource) => {
    // CSV 생성
    const csv = unparse(records, { delimiter: "," });

    // BOM 추가하여 한글 깨짐 방지
    const blob = new Blob(["\uFEFF" + csv], {
        type: "text/csv;charset=utf-8;",
    });

    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `${resource}.csv`;
    a.click();
};

/* -------------------------------------------------
 *  BulkExportButton Component
 * ------------------------------------------------- */
export const BulkExportButton = <T extends RaRecord>({
                                                         icon = defaultIcon,
                                                         label = "ra.action.export",
                                                         onClick,
                                                         exporter = csvExporter, // ✔ 기본 exporter 교체!
                                                         ...props
                                                     }: BulkExportButtonProps<T>) => {
    const bulkExport = useBulkExport({ ...props, exporter });

    const handleClick = (event: React.MouseEvent<HTMLButtonElement>) => {
        bulkExport();
        onClick?.(event);
    };

    return (
        <Button
            onClick={handleClick}
            role="button"
            variant="outline"
            size="sm"
            className="flex items-center gap-2 h-9"
            {...sanitizeRestProps(props)}
        >
            {icon}
            {label && <Translate i18nKey={label}>{label}</Translate>}
        </Button>
    );
};

const defaultIcon = <Download className="h-4 w-4" />;

export type BulkExportButtonProps<T extends RaRecord> =
    UseBulkExportOptions<T> & {
    icon?: React.ReactNode;
    label?: string;
} & React.ComponentProps<typeof Button>;

const sanitizeRestProps = <T extends RaRecord>({
                                                   resource: _resource,
                                                   exporter: _exporter,
                                                   onClick: _onClick,
                                                   label: _label,
                                                   icon: _icon,
                                                   meta: _meta,
                                                   ...rest
                                               }: BulkExportButtonProps<T>) => rest;
