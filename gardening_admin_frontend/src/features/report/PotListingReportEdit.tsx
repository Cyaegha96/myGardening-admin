import {
    Edit,
    SimpleForm,
    TextInput,
    SelectInput,

    NumberInput,
     ReferenceField, TextField,
} from "@/shared/shadcn/components/admin";
export const PotlistingreportEdit = () => (
    <Edit>
        <SimpleForm>
            <NumberInput  source="id" disabled />
            <NumberInput  source="potListingId" disabled />
            <SelectInput
                source="status"
                label="상태"
                choices={[
                    { id: "pending", name: "처리대기" },
                    { id: "approved", name: "승인됨" },
                    { id: "rejected", name: "거절됨" },
                ]}
            />
            <TextInput source="reason" disabled/>

            <ReferenceField source="reporterUid" reference="users" link="show">
                <TextField source="nickname" /> 의 제보!
            </ReferenceField>
        </SimpleForm>
    </Edit>
);