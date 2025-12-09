import {
    Edit,
    SimpleForm,
    TextInput,
    SelectInput,
    ReferenceInput,
    NumberInput,
} from "@/shared/shadcn/components/admin";

export const ReportEdit = () => (
    <Edit>
        <SimpleForm>

            {/* ID (read-only) */}
            <NumberInput source="id" label="ID" disabled />

            {/* 사유 (수정 가능) */}
            <TextInput source="reason" label="사유" />

            {/* 제보자 (수정 불가) */}
            <ReferenceInput
                source="reporterUid"
                reference="users"
                label="제보자"
            >
                <SelectInput optionValue="id" optionText="nickname" disabled />
            </ReferenceInput>

            {/* 상태 (수정 가능) */}
            <SelectInput
                source="status"
                label="상태"
                choices={[
                    { id: "pending", name: "처리대기" },
                    { id: "approved", name: "승인됨" },
                    { id: "rejected", name: "거절됨" },
                ]}
            />

            {/* 문제 게시글 (수정 불가) */}
            <ReferenceInput
                source="targetId"
                reference="boards"
                label="문제 게시글"
            >
                <SelectInput optionValue="id" optionText="title" disabled />
            </ReferenceInput>

            {/* 타입 (수정 불가) */}
            <SelectInput
                source="targetType"
                label="타깃 타입"
                choices={[
                    { id: "BOARD", name: "게시글" },
                    { id: "BOARD_COMMENT", name: "댓글" },
                ]}
                disabled
            />
        </SimpleForm>
    </Edit>
);
