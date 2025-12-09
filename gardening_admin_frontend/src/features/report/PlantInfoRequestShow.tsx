import {NumberField, RecordField, Show} from "@/shared/shadcn/components/admin";

export const PlantinforequestShow = () => (
    <Show>
        <div className="flex flex-col gap-4">
            <RecordField source="id">
                <NumberField source="id" />
            </RecordField>
            <RecordField
                source="files"
                label="첨부 이미지"
                render={(record) => (
                    <div className="flex gap-4 flex-wrap">
                        {record.files?.length > 0 ? (
                            record.files.map((file: any) => (
                                <img
                                    key={file.id}
                                    src={file.url}  // 실제 파일 서버 경로 적용
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
            <RecordField source="scientificName" />
            <RecordField source="changes" />
            <RecordField source="reviewerUid" />
            <RecordField source="reviewNote" />
            <RecordField source="status" />
            <RecordField source="createdAt"
                render={(record) =>
                record.createdAt
                ? new Date(
                record.createdAt[0],       // year
                record.createdAt[1] - 1,   // month (0-index)
                record.createdAt[2],       // day
                record.createdAt[3],
                record.createdAt[4],
                record.createdAt[5]
                ).toLocaleString("ko-KR")
                : ""
            }/>
            <RecordField source="updatedAt"     render={(record) =>
                record.updatedAt
                    ? new Date(
                        record.updatedAt[0],       // year
                        record.updatedAt[1] - 1,   // month (0-index)
                        record.updatedAt[2],       // day
                        record.updatedAt[3],
                        record.updatedAt[4],
                        record.updatedAt[5]
                    ).toLocaleString("ko-KR")
                    : ""
            }/>
            <RecordField source="userUid" />
        </div>
    </Show>
);