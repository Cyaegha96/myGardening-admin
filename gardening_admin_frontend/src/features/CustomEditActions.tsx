
import {Button} from "@/shared/shadcn/components/ui/button.tsx";
import { useRedirect, useRecordContext, useDelete} from "ra-core";

export const ShowButton = ({ resource }: { resource: string }) => {
    const redirect = useRedirect();
    const record = useRecordContext();

    if (!record) return null;

    return (
        <Button
            variant="outline"
            onClick={() => redirect("show", resource, record.id)}
        >
            보기
        </Button>
    );
};

// 재사용 가능한 CustomEditActions
export const CustomEditActions = ({ resource }: { resource: string }) => {
    const record = useRecordContext();
    const [deleteOne] = useDelete();
    const redirect = useRedirect();

    const handleDelete = () => {
        if (!record) return;

        const confirmed = window.confirm(
            "실제 DB에 있는 데이터가 삭제됩니다. 이 작업은 되돌릴 수 없습니다. 정말로 삭제하시겠습니까?"
        );
        if (confirmed) {
            deleteOne(resource, { id: record.id });
            redirect("list", resource); // 삭제 후 해당 리소스 목록으로 이동
        }
    };

    return (
        <div className="flex gap-2">
            <ShowButton resource={resource} />
            <Button variant="destructive" onClick={handleDelete}>
                삭제
            </Button>
        </div>
    );
};
