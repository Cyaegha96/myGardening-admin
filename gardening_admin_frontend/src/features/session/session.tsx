import {

    DataTable, DeleteButton,
    List,

} from "@/shared/shadcn/components/admin"

export const SessionList = () => (
    <List>
        <DataTable>
            <DataTable.Col source="id" />
            <DataTable.Col source="uid" />
            <DataTable.Col source="nickname" />
            <DataTable.Col source="provider" />
            <DataTable.Col source="ip" />
            <DataTable.Col
                label="Actions"
                render={(record) => (
                    <DeleteButton resource="sessions" record={record} mutationMode="pessimistic" />
                )}
            />
        </DataTable>
    </List>
);
