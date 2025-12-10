import {NumberField, RecordField, ReferenceField, Show, } from "@/shared/shadcn/components/admin";

export const PotListingReportShow = () => (
    <Show>
        <div className="flex flex-col gap-4">
            <RecordField source="id">
                <NumberField source="id" />
            </RecordField>

            <RecordField source="potListingId"/>

            <RecordField source="reason" />
            <ReferenceField source="reporterUid" reference="users" link="show">
                <RecordField source="nickname" label="제보자" />
            </ReferenceField>
            <RecordField source="status" />
            <RecordField source="createdAt"  render={(record) =>
                record.createdAt
                    ? new Date(
                        record.createdAt[0],       // year
                        record.createdAt[1] - 1,   // month (0-index)
                        record.createdAt[2],       // day
                        record.createdAt[3],
                        record.createdAt[4],
                        record.createdAt[5]
                    ).toLocaleString("ko-KR")
                    : ""
            }/>
        </div>
    </Show>
);