import {
    Edit,
    SimpleForm,
    TextInput,
    SelectInput,
    ReferenceInput,
    NumberInput,
    RecordField,
} from "@/shared/shadcn/components/admin";

export const PlantinforequestEdit = () => (
    <Edit mutationMode="pessimistic">
        <SimpleForm>


            <RecordField
                source="files"
                label="첨부 이미지"
                render={(record) => (
                    <div className="flex gap-4 flex-wrap">
                        {record?.files?.length > 0 ? (
                            record.files.map((file: any) => (
                                <img
                                    key={file.id}
                                    src={file.url}
                                    alt={file.oriName}
                                    className="w-40 h-40 object-cover rounded-lg border"
                                />
                            ))
                        ) : (
                            <p className="text-gray-500">첨부 이미지 없음</p>
                        )}
                    </div>
                )}
            />

            <NumberInput source="id" label="ID" disabled />
            <TextInput source="scientificName" label="학명" disabled />
            <TextInput
                source="changes"
                label="변경사항"
                InputProps={{ readOnly: true }}
            />
            <TextInput
                source="reviewerUid"
                label="리뷰어 UID"
                placeholder="업데이트 시 자동 기록"
                disabled
            />


            <TextInput
                source="reviewNote"
                label="리뷰 노트"
                multiline
            />

            <SelectInput
                source="status"
                label="상태"
                choices={[
                    { id: "pending", name: "처리대기" },
                    { id: "approved", name: "승인됨" },
                    { id: "rejected", name: "거절됨" },
                ]}
            />

            {/* 📌 제보자 정보 (읽기 전용) */}
            <ReferenceInput
                source="userUid"
                reference="users"
                label="제보자"
            >
                <SelectInput optionValue="id" optionText="nickname" disabled />
            </ReferenceInput>
        </SimpleForm>
    </Edit>
);
