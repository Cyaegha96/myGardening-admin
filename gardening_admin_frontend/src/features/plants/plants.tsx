import {DataTable, List, } from "@/shared/shadcn/components/admin";
import { Create, Edit, SimpleForm, TextInput , Show,} from "@/shared/shadcn/components/admin";
import {required, useRecordContext} from "ra-core";
import { Card, CardContent, CardHeader, CardTitle } from "@/shared/shadcn/components/ui/card";
import {FileUploadInput} from "@/widgets/FileUploadInput.tsx";

const plantFilters = [
    <TextInput key="email" source="scientificName" label="학명 검색" />,
    <TextInput key="name" source="commonName" label="이름 검색" />,
    <TextInput key="nickname" source="family" label="Family 과 검색" />,
    <TextInput key="nickname" source="genus" label="Genus 속 검색" />,
   

];

export const PlantList = (props: any) => (
    <List {...props}  filters={plantFilters}>
        <DataTable>
            <DataTable.Col
                source="scientificName"
                label="Scientific Name (ID)"
            />
            <DataTable.Col source="commonName" label="Common Name" />
            <DataTable.Col source="family" label="Family" />
            <DataTable.Col source="genus" label="Genus" />

            <DataTable.Col
                source="sampleImageUrl"
                label="Image"
                render={(record) => (
                    <img
                        src={record.sampleImageUrl}
                        alt="식물사진"
                        className="w-12 h-12 object-cover rounded"
                    />
                )}
            />
        </DataTable>
    </List>
);



export const PlantEdit = () => (
    <Edit>
        <SimpleForm>
            <TextInput source="scientificName" disabled />
            <TextInput source="commonName" />
            <TextInput source="family" />
            <TextInput source="genus" />
            <TextInput source="origin" />
            <TextInput source="description" multiline />
            <TextInput source="environment" multiline />
            <TextInput source="light" multiline />
            <TextInput source="soil" multiline />
            <TextInput source="watering" multiline />
            <TextInput source="temperatureHumidity" multiline />
            <TextInput source="fertilizer" multiline />
            <TextInput source="potRepot" multiline />
            <TextInput source="propagation" multiline />
            <TextInput source="pestsTips" multiline />
            <TextInput source="commonUses" multiline />
            <TextInput source="culturalSignificance" multiline />
            <FileUploadInput source="sampleImageUrl" label="Sample Image" />
        </SimpleForm>
    </Edit>
);

export const PlantCreate = () => (
    <Create>
        <SimpleForm>
            <TextInput source="scientificName" validate={required()}/>
            <TextInput source="commonName" validate={required()}/>
            <TextInput source="family" validate={required()}/>
            <TextInput source="genus" validate={required()}/>
            <TextInput source="origin" validate={required()}/>
            <TextInput source="description" multiline validate={required()}/>
            <TextInput source="environment" multiline validate={required()}/>
            <TextInput source="light" multiline validate={required()} />
            <TextInput source="soil" multiline validate={required()}/>
            <TextInput source="watering" multiline validate={required()}/>
            <TextInput source="temperatureHumidity" multiline validate={required()}/>
            <TextInput source="fertilizer" multiline validate={required()}/>
            <TextInput source="potRepot" multiline validate={required()}/>
            <TextInput source="propagation" multiline validate={required()}/>
            <TextInput source="pestsTips" multiline validate={required()}/>
            <TextInput source="commonUses" multiline validate={required()}/>
            <TextInput source="culturalSignificance" multiline validate={required()}/>
            <FileUploadInput source="sampleImageUrl" label="Sample Image" />
        </SimpleForm>
    </Create>
);

export const PlantShow = () => (
    <Show>
        <PlantShowContent />
    </Show>
);

const FieldRow = ({ label, value }: { label: string; value?: any }) => (
    <div className="flex justify-between py-2 border-b">
        <span className="font-medium text-sm text-gray-600">{label}</span>
        <span className="text-sm text-gray-900">{value || "-"}</span>
    </div>
);

const PlantShowContent = () => {
    const record = useRecordContext();

    if (!record) return null;

    return (
        <div className="max-w-3xl mx-auto space-y-6">

            {/* 이미지 카드 */}
            <Card>
                <CardHeader>
                    <CardTitle>{record.commonName ?? record.scientificName}</CardTitle>
                </CardHeader>
                <CardContent className="flex justify-center">
                    <img
                        src={record.sampleImageUrl}
                        alt="plant"
                        className="w-48 h-48 object-cover rounded shadow"
                    />
                </CardContent>
            </Card>

            {/* 기본 정보 */}
            <Card>
                <CardHeader>
                    <CardTitle>Basic Information</CardTitle>
                </CardHeader>
                <CardContent>
                    <FieldRow label="Scientific Name" value={record.scientificName} />
                    <FieldRow label="Common Name" value={record.commonName} />
                    <FieldRow label="Family" value={record.family} />
                    <FieldRow label="Genus" value={record.genus} />
                    <FieldRow label="Origin" value={record.origin} />
                </CardContent>
            </Card>

            {/* 상세 정보 */}
            <Card>
                <CardHeader>
                    <CardTitle>Details</CardTitle>
                </CardHeader>
                <CardContent className="space-y-4">
                    <LongText label="Description" value={record.description} />
                    <LongText label="Environment" value={record.environment} />
                    <LongText label="Light" value={record.light} />
                    <LongText label="Soil" value={record.soil} />
                    <LongText label="Watering" value={record.watering} />
                    <LongText label="Temperature / Humidity" value={record.temperatureHumidity} />
                    <LongText label="Fertilizer" value={record.fertilizer} />
                    <LongText label="Pot / Repot" value={record.potRepot} />
                    <LongText label="Propagation" value={record.propagation} />
                    <LongText label="Pests & Tips" value={record.pestsTips} />
                    <LongText label="Common Uses" value={record.commonUses} />
                    <LongText label="Cultural Significance" value={record.culturalSignificance} />
                </CardContent>
            </Card>
        </div>
    );
};

const LongText = ({ label, value }: { label: string; value?: string }) => (
    <div>
        <div className="font-medium text-gray-700 text-sm mb-1">{label}</div>
        <div className="text-gray-900 text-sm whitespace-pre-wrap p-3 rounded bg-gray-50 border">
            {value || "-"}
        </div>
    </div>
);
