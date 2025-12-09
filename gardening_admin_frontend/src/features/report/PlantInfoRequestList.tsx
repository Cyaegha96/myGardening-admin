import {
    List,
    DataTable,
    ReferenceField, TextField
} from "@/shared/shadcn/components/admin";
import {Badge} from "@/shared/shadcn/components/ui/badge.tsx";
export const PlantinforequestList = () => (
    <List>
        <DataTable>
            <DataTable.Col source="id" label="ID" />
            <DataTable.Col label="제보자">
                <ReferenceField source="userUid" reference="users" link="show">
                    <TextField source="nickname" />
                </ReferenceField>
            </DataTable.Col>
            <DataTable.Col label="대상식물">
                <ReferenceField source="scientificName" reference="plants" link="show">
                    <TextField source="commonName" />
                </ReferenceField>
            </DataTable.Col>

            <DataTable.Col source="changes" />
            <DataTable.Col source="status"   label="상태"
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
            <DataTable.Col label="제보자">
                <ReferenceField source="reviewerUid" reference="users" link="show">
                    <TextField source="nickname" />
                </ReferenceField>
            </DataTable.Col>
            <DataTable.Col source="reviewNote" />


        </DataTable>
    </List>
);