import {DataTable, List, TextInput} from "@/shared/shadcn/components/admin";
import {Badge} from "@/shared/shadcn/components/ui/badge.tsx";

const boardFilters = [
    <TextInput key="email" source="title" label="제목 검색" />,
    <TextInput key="name" source="contents" label="내용 검색" />,
    <TextInput key="nickname" source="nickname" label="닉네임 검색" />,


];

export const BoardList = () => {
    return (
        <List filters={boardFilters}>
            <DataTable

            >
                {/* ID */}
                <DataTable.Col source="id" label="ID" />

                {/* 제목 */}
                <DataTable.Col source="title" label="제목" />

                {/* 작성자 (nickname) */}
                <DataTable.Col source="nickname" label="작성자" />

                {/* 조회수 */}
                <DataTable.Col source="viewCount" label="조회수">

                </DataTable.Col>

                {/* 공지 여부 */}
                <DataTable.Col source="isNotification" label="공지"
                               render={ (record) =>
                                   record.isNotification === "Y" ? (
                                       <Badge className="text-xs px-2 py-0.5">공지</Badge>
                                   ) : (
                                       "-"
                                   )
                               }/>



                {/* 상태 */}
                <DataTable.Col source="status" label="상태"

                               render={(record) => {
                                   const status = record.status;

                                   const variant =
                                       status === "active"
                                           ? "default"
                                           : status === "delete"
                                                   ? "destructive"
                                                   : "outline";

                                   const label =
                                       status === "active"
                                           ? "활성"
                                           : status === "delete"
                                               ? "삭제"
                                              : "-";

                                   return <Badge variant={variant}>{label}</Badge>;
                               }}
                />

                {/* 작성일 */}
                <DataTable.Col source="createdAt" label="작성일"
                               render={(record) =>
                                   record.createdAt
                                       ? new Date(record.createdAt).toLocaleString("ko-KR", {
                                           year: "numeric",
                                           month: "2-digit",
                                           day: "2-digit",
                                           hour: "2-digit",
                                           minute: "2-digit",
                                           second: "2-digit",
                                       })
                                       : ""
                               }>

                </DataTable.Col>
            </DataTable>
        </List>
    );
};
