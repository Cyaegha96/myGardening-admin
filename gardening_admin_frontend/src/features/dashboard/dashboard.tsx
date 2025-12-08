import {useEffect,  useState} from "react";
import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
} from "@/shared/shadcn/components/ui/card";

import { UserStatsChart } from "@/features/dashboard/UserStatsChart.tsx";
import axiosInterceptor from "@/shared/api/axiosInterceptor.ts";
import {Button} from "@/shared/shadcn/components/ui/button.tsx";
import {Badge} from "@/shared/shadcn/components/ui/badge.tsx";
import {RecordContextProvider, ResourceContextProvider} from "ra-core";
import {ReferenceField, TextField} from "@/shared/shadcn/components/admin";

const LOGS_PER_PAGE = 5; // 한 페이지에 표시할 로그 수

export const PlantNetQuotaCard = () => {
    const [quota, setQuota] = useState<{ count: number; total: number; remaining: number } | null>(null);
    const [logs, setLogs] = useState<{ nickname: string; matchedScientificName: string; url?: string; commonName: string , userUid:string}[]>([]);

    const [selectedDate, setSelectedDate] = useState(() => {
        const today = new Date();
        return today.toISOString().split("T")[0]; // YYYY-MM-DD
    });
    const [loadingQuota, setLoadingQuota] = useState(false);
    const [loadingLogs, setLoadingLogs] = useState(false);
    const [currentPage, setCurrentPage] = useState(1);

    const fetchQuota = async (date: string) => {
        setLoadingQuota(true);
        try {
            const res = await axiosInterceptor.get("/stats/plantnet", {
                params: { day: date },
            });
            setQuota(res.data.quota.identify);
        } catch (err) {
            console.error("Quota API 호출 실패:", err);
        } finally {
            setLoadingQuota(false);
        }
    };

    const fetchLogs = async (date: string) => {
        setLoadingLogs(true);
        try {
            const res = await axiosInterceptor.get("/stats/searchPlantLog", {
                params: { day: date },
            });
            const filteredLogs = res.data.map((log: any) => ({
                nickname: log.nickname,
                userUid:log.userUid,
                matchedScientificName: log.matchedScientificName,
                url: log.url,
                commonName: log.commonName,
            }));
            setLogs(filteredLogs);
            setCurrentPage(1); // 날짜 바뀌면 1페이지로 초기화
        } catch (err) {
            console.error("Plant Search 로그 조회 실패:", err);
        } finally {
            setLoadingLogs(false);
        }
    };

    useEffect(() => {
        fetchQuota(selectedDate);
        fetchLogs(selectedDate);
    }, [selectedDate]);

    // 페이지네이션 처리
    const totalPages = Math.ceil(logs.length / LOGS_PER_PAGE);
    const paginatedLogs = logs.slice((currentPage - 1) * LOGS_PER_PAGE, currentPage * LOGS_PER_PAGE);

    return (
        <Card>
            <CardHeader>
                <CardTitle>PlantNet API 호출 정보</CardTitle>
            </CardHeader>
            <CardContent>
                {/* 날짜 선택 */}
                <div className="mb-2 flex items-center gap-2">
                    <input
                        type="date"
                        value={selectedDate}
                        onChange={(e) => setSelectedDate(e.target.value)}
                        className="border px-2 py-1 rounded"
                    />
                    <Button onClick={() => { fetchQuota(selectedDate); fetchLogs(selectedDate); }}>
                        조회
                    </Button>
                </div>

                {/* Quota 표시 */}
                {loadingQuota && <p className="text-sm text-gray-500">Quota 불러오는 중...</p>}
                {quota && (
                    <div className="mt-2 text-sm text-gray-700">
                        <p>오늘 요청 횟수: {quota.count} / {quota.total}</p>
                        <p>남은 횟수: {quota.remaining}</p>
                    </div>
                )}

                {/* Logs 표시 */}
                <div className="mt-4">
                    <h4 className="font-semibold text-sm mb-1">Plant Search 요청 로그</h4>
                    {loadingLogs && <p className="text-sm text-gray-500">로그 불러오는 중...</p>}
                    {!loadingLogs && logs.length === 0 && <p className="text-sm text-gray-500">해당 날짜 로그 없음</p>}
                    {!loadingLogs && logs.length > 0 && (

                        <>
                            <div className="space-y-2">
                                {paginatedLogs.map((log, idx) => {
                                    return (
                                    <div
                                        key={idx}
                                        className="flex items-center gap-4 p-2 bg-white border rounded shadow-sm hover:shadow-md transition-shadow"
                                    >
                                        {/* 이미지 */}
                                        {log.url && (
                                            <img
                                                src={log.url}
                                                alt={log.matchedScientificName}
                                                className="w-16 h-16 object-cover rounded border"
                                            />
                                        )}

                                        {/* 텍스트 */}
                                        <div className="flex-1">
                                            <ResourceContextProvider value="users">
                                                <RecordContextProvider value={log}>
                                                    <ReferenceField source="userUid" reference="users" link="show">
                                                        <Badge variant="secondary">
                                                            <TextField source="nickname" />
                                                        </Badge>
                                                    </ReferenceField>
                                                </RecordContextProvider>
                                            </ResourceContextProvider>

                                            <ResourceContextProvider value="plants">
                                                <RecordContextProvider value={log}>
                                                    <ReferenceField
                                                        source="matchedScientificName"
                                                        reference="plants"
                                                        link="show"
                                                    >
                                                        <TextField source="scientificName" />
                                                        <p className="text-sm text-gray-500"> <TextField source="commonName" /></p>
                                                    </ReferenceField>
                                                </RecordContextProvider>
                                            </ResourceContextProvider>

                                        </div>
                                    </div>
                                    ) })}
                            </div>

                            {/* 페이지네이션 버튼 */}
                            <div className="mt-2 flex gap-2">
                                <Button
                                    disabled={currentPage === 1}
                                    onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
                                >
                                    이전
                                </Button>
                                <span className="self-center text-sm">
                                    {currentPage} / {totalPages}
                                </span>
                                <Button
                                    disabled={currentPage === totalPages}
                                    onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
                                >
                                    다음
                                </Button>
                            </div>
                        </>
                    )}
                </div>
            </CardContent>
        </Card>
    );
};



export const Dashboard = () => {

    return (
        <div className="flex flex-col md:flex-row gap-4 mb-4">
            <div className="flex flex-col gap-4 md:basis-1/2">
                <Card>
                    <CardHeader>
                        <CardTitle>사용자 통계</CardTitle>
                    </CardHeader>
                    <CardContent>
                        <UserStatsChart />
                    </CardContent>
                </Card>
            </div>
            <div className="md:basis-1/2">
                <PlantNetQuotaCard/>
            </div>
        </div>
    );
};