import {
    DateField, Edit,
  SelectInput, SimpleForm, TextInput,

} from "@/shared/shadcn/components/admin";
import {useRecordContext} from "ra-core";
import {Card, CardContent, CardHeader, CardTitle} from "@/shared/shadcn/components/ui/card.tsx";
import {Separator} from "@radix-ui/react-select";



export const BoardEdit = () => {
    return (
        <Edit>
            <BoardEditForm />
        </Edit>
    );
};

const BoardEditForm = () => {
    const record = useRecordContext();

    return (
        <SimpleForm className="space-y-6">

            {/* 제목 */}
            <Card>
                <CardHeader>
                    <CardTitle>게시글 수정</CardTitle>
                </CardHeader>

                <CardContent className="space-y-4">

                    <TextInput source="title" label="제목" />

                    <SelectInput
                        source="status"
                        label="상태"
                        choices={[
                            { id: "active", name: "active" },
                            { id: "delete", name: "delete" },
                        ]}

                    />

                    <SelectInput
                        source="isNotification"
                        label="공지 여부"
                        choices={[
                            { id: "Y", name: "공지" },
                            { id: "N", name: "일반" },
                        ]}

                    />

                    <TextInput
                        source="contents"
                        label="내용"
                        multiline

                    />

                    {/* 읽기 전용 정보 */}
                    <Separator className="my-4" />
                    <div className="text-sm text-muted-foreground">
                        <div>작성자 UID: {record?.writerUid}</div>
                        <div>작성일: <DateField source="createdAt" /></div>
                    </div>

                </CardContent>
            </Card>

        </SimpleForm>
    );
};
