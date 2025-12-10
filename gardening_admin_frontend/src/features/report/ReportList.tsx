import {
    List,
    DataTable,
    ReferenceField, TextField
} from "@/shared/shadcn/components/admin";
import {Badge} from "@/shared/shadcn/components/ui/badge.tsx";


export const ReportList = () => (
    <List>
        <DataTable>
            <DataTable.Col source="id"  label="ID"  />
            <DataTable.Col source="reason"  label="사유"  />

            <DataTable.Col label="제보자">
                <ReferenceField source="reporterUid" reference="users" link="show">
                    <TextField source="nickname" />
                </ReferenceField>
            </DataTable.Col>
            <DataTable.Col source="status"   label="상태"
            render=   {(record) => {
                const value = record.status;

                const map: Record<string, { label: string; color: string }> = {
                    pending:  { label: "처리대기", color: "outline" },
                    approved: { label: "승인됨",   color: "secondary" },
                    rejected: { label: "거절됨",   color: "destructive" },
                };

                const status = map[value] ?? { label: value, color: "secondary" };

                // @ts-ignore
                return <Badge variant={status.color}>{status.label}</Badge>;}}
                />


            <DataTable.Col label="문제게시글">
                <ReferenceField source="targetId" reference="boards" link="show">
                    <TextField source="title"   />

                </ReferenceField>
            </DataTable.Col>
            <DataTable.Col source="targetType"  label="타깃 타입" />
        </DataTable>
    </List>
);