import {
    NumberField,
    RecordField,
    ReferenceField,
    Show,
} from "@/shared/shadcn/components/admin";

export const ReportShow = () => (
    <Show>
        <div className="flex flex-col gap-4">
            <RecordField source="id">
                <NumberField source="id" />
            </RecordField>

            <RecordField source="reason" />

            {/* reporterUid → users.nickname 매핑 */}
            <ReferenceField source="reporterUid" reference="users" link="show">
                <RecordField source="nickname" label="제보자" />
            </ReferenceField>

            <RecordField source="status" />

            {/* targetId → boards.title 매핑 */}
            <ReferenceField source="targetId" reference="boards" link="show">
                <RecordField source="title" label="문제 게시글" />
            </ReferenceField>

            <RecordField source="targetType" />
        </div>
    </Show>
);
