import {
    List,

    DataTable, ReferenceField, TextField,

} from "@/shared/shadcn/components/admin";
import {Badge} from "@/shared/shadcn/components/ui/badge.tsx";



export const PotlistingreportList = () => (
    <List>
        <DataTable>
            <DataTable.Col source="id" />
            <DataTable.Col label="제보자">
                <ReferenceField source="reporterUid" reference="users" link="show">
                    <TextField source="nickname" />
                </ReferenceField>
            </DataTable.Col>

            <DataTable.Col source="potListingId">
            </DataTable.Col>
            <DataTable.Col source="reason" />

            <DataTable.Col source="status" label="상태"
                           render=   {(record) => {
                               const value = record.status;

                               const map: Record<string, { label: string; color: string }> = {
                                   pending:  { label: "처리대기", color: "outline" },
                                   approved: { label: "승인됨",   color: "secondary" },
                                   rejected: { label: "거절됨",   color: "destructive" },
                               };

                               const status = map[value] ?? { label: value, color: "secondary" };

                               return <Badge variant={status.color}>{status.label}</Badge>;}}
            />
            <DataTable.Col source="createdAt"   render={(record) =>
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
        </DataTable>
    </List>
);