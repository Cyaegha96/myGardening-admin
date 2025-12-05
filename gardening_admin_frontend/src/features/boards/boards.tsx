import {
    DateField,
    NumberField,Show,TextField,

} from "@/shared/shadcn/components/admin";


import { Card, CardHeader, CardTitle, CardContent } from "@/shared/shadcn/components/ui/card";
import { Separator } from "@/shared/shadcn/components/ui/separator";
import {useRecordContext} from "ra-core";

export const BoardShow = () => {
    return (
        <Show>
            <BoardShowContent />
        </Show>
    );
};

const BoardShowContent = () => {
    return (
        <div className="flex flex-col gap-6">


            <Card>
                <CardHeader>
                    <CardTitle className="text-xl font-bold">
                        <TextField source="title" />
                    </CardTitle>
                </CardHeader>

                <CardContent className="space-y-4">
                    {/* 작성자/작성일/조회수 */}
                    <div className="flex items-center gap-4 text-sm text-muted-foreground">
                        <span>작성자: <TextField source="nickname" /></span>
                        <Separator orientation="vertical" />
                        <span><DateField source="createdAt" /></span>
                        <Separator orientation="vertical" />
                        <span>조회수: <NumberField source="viewCount" /></span>
                    </div>


                    {/* 본문 내용 */}
                    <div className="prose dark:prose-invert">
                        <BoardImages />
                        <TextField source="contents" />
                    </div>
                </CardContent>
            </Card>

            <BoardComments />

        </div>
    );
};
const BoardImages = () => {
    const record = useRecordContext();
    if (!record || !record.files || record.files.length === 0) return null;

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
            {record.files.map((file: any) => (
                <Card key={file.id} className="rounded-xl overflow-hidden shadow">
                    <CardContent className="p-0">
                        <img
                            src={file.url}
                            alt={file.oriName}
                            className="w-full h-64 object-cover"
                        />
                    </CardContent>
                </Card>
            ))}
        </div>
    );
};

const BoardComments = () => {
    const record = useRecordContext();
    if (!record || !record.comments || record.comments.length === 0) return null;

    const comments = record.comments;

    // 트리 구조 생성
    const buildTree = () => {
        const map = new Map();
        const roots:any = [];

        comments.forEach((c: any) => {
            map.set(c.id, { ...c, children: [] });
        });

        comments.forEach((c: any) => {
            if (c.refCommentId) {
                map.get(c.refCommentId)?.children.push(map.get(c.id));
            } else {
                roots.push(map.get(c.id));
            }
        });

        return roots;
    };

    const tree = buildTree();

    return (
        <Card>
            <CardHeader>
                <CardTitle>댓글</CardTitle>
            </CardHeader>

            <CardContent>
                <CommentList comments={tree} depth={0} />
            </CardContent>
        </Card>
    );
};


const CommentList = ({ comments, depth }: { comments: any[]; depth: number }) => {
    return (
        <div className="space-y-3">
            {comments.map((comment) => (
                <CommentItem key={comment.id} comment={comment} depth={depth} />
            ))}
        </div>
    );
};


const CommentItem = ({ comment, depth }: { comment: any; depth: number }) => {
    return (
        <div
            className="p-3 rounded-md bg-muted/40"
            style={{
                marginLeft: depth * 24,            // 들여쓰기
                borderLeft: depth > 0 ? "2px solid hsl(var(--muted-foreground))" : "none",
            }}
        >
            {/* 상단: 작성자 + 날짜 */}
            <div className="flex items-center justify-between mb-1">
                <span className="font-medium">{comment.nickname}</span>
                <DateField record={comment} source="createdAt" className="text-sm" />
            </div>

            {/* 내용 */}
            <p className="whitespace-pre-line text-sm">{comment.contents}</p>

            {/* 대댓글 */}
            {comment.children && comment.children.length > 0 && (
                <CommentList comments={comment.children} depth={depth + 1} />
            )}
        </div>
    );
};
