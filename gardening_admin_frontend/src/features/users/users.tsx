import {
    DataTable,

    List,
    SimpleForm,
    TextInput,
    DateField,
    Show,
    RecordField,
    DateInput, SelectInput, SimpleFormIterator, ArrayInput,
} from "@/shared/shadcn/components/admin";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/shared/shadcn/components/ui/tabs";
import { Edit} from "@/shared/shadcn/components/admin/edit";
import { Badge } from "@/shared/shadcn/components/ui/badge";
import type {UserAdminViewDTO} from "@/entities/users/UserAdminViewDTO.tsx";
import {required,  useGetList, type ChoicesProps, type InputProps} from "ra-core";
import {CustomEditActions} from "@/features/CustomEditActions.tsx";
import {FileUploadInput} from "@/widgets/FileUploadInput.tsx";
import type {SupportCreateSuggestionOptions} from "@/hooks/useSupportCreateSuggestion";
import type {ReactElement, DetailedHTMLProps, HTMLAttributes} from "react";
import type {JSX} from "react/jsx-runtime";


const userFilters = [
    <TextInput key="email" source="email" label="이메일 검색"/>,
    <TextInput key="name" source="name" label="이름 검색"/>,
    <TextInput key="nickname" source="nickname" label="닉네임 검색"/>,
    <SelectInput
        key="status"
        source="status"
        label="가입 상태"
        choices={[
            {id: 'ACTIVE', name: '활성'},
            {id: 'INACTIVE', name: '탈퇴'},
            {id: 'BLOCKED', name: '차단'},
        ]}
    />,

];

export const UserList = (props: any) => (
    <List {...props} filters={userFilters}>
        <Tabs defaultValue="account" className="w-full">
            <TabsList>
                <TabsTrigger value="account">계정 정보</TabsTrigger>
                <TabsTrigger value="info">사용자 정보</TabsTrigger>
                <TabsTrigger value="personal">개인 정보</TabsTrigger>
            </TabsList>

            <TabsContent value="account">
                <DataTable<UserAdminViewDTO>>
                    <DataTable.Col source="id" label="UUID"/>
                    <DataTable.Col source="userId" label="아이디"/>
                    <DataTable.Col source="providerUserId" label="oauth2아이디"/>
                    <DataTable.Col source="provider" label="로그인 제공자"/>
                    <DataTable.Col
                        source="status"
                        label="가입 상태"
                        render={(record) => {
                            const status = record.status;

                            const variant =
                                status === "ACTIVE"
                                    ? "default"
                                    : status === "INACTIVE"
                                        ? "destructive"
                                        : status === "BLOCKED"
                                            ? "destructive"
                                            : "outline";

                            const label =
                                status === "ACTIVE"
                                    ? "활성"
                                    : status === "INACTIVE"
                                        ? "탈퇴"
                                        : status === "BLOCKED"
                                            ? "차단"
                                            : status ?? "-";

                            return <Badge variant={variant}>{label}</Badge>;
                        }}
                    />
                    <DataTable.Col source="roles"
                                   label="권한"
                                   render={(record) => (
                                       <div style={{display: "flex", gap: "8px"}}>
                                           {record.roles?.map((role: any) => (
                                               <span key={role.roleId}
                                                     style={{
                                                         padding: "4px 8px",
                                                         background: "#eee",
                                                         borderRadius: "12px"
                                                     }}>
                             {role.roleName}
                         </span>
                                           ))}
                                       </div>
                                   )}
                    />
                    <DataTable.Col
                        source="createdAt"
                        label="생성일"
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
                        }
                    />

                    <DataTable.Col
                        source="updatedAt"
                        label="수정일"
                        render={(record) =>
                            record.updatedAt
                                ? new Date(record.updatedAt).toLocaleString("ko-KR", {
                                    year: "numeric",
                                    month: "2-digit",
                                    day: "2-digit",
                                    hour: "2-digit",
                                    minute: "2-digit",
                                    second: "2-digit",
                                })
                                : ""
                        }
                    />

                </DataTable>
            </TabsContent>

            <TabsContent value="info">
                <DataTable<UserAdminViewDTO>>
                    <DataTable.Col source="id" label="UUID"/>
                    <DataTable.Col source="name" label="이름"/>
                    <DataTable.Col source="nickname" label="닉네임"/>
                    <DataTable.Col source="bio" label="소개"/>
                    <DataTable.Col
                        source="profileUrl"
                        label="프로필 이미지"
                        render={(record) => (
                            (record.profileUrl && <img
                                    src={record.profileUrl}
                                    alt="프로필 이미지"
                                    style={{width: 40, height: 40, borderRadius: "50%"}}
                                />
                            )
                        )}
                    />
                </DataTable>
            </TabsContent>

            <TabsContent value="personal">
                <DataTable<UserAdminViewDTO>>
                    <DataTable.Col source="id" label="UUID"/>
                    <DataTable.Col source="email" label="이메일"/>
                    <DataTable.Col source="phone" label="전화번호"/>
                    <DataTable.Col source="address" label="주소"/>
                    <DataTable.Col source="addressDetail" label="상세주소"/>
                    <DataTable.Col source="zipcode" label="우편번호"/>
                    <DataTable.Col source="birthDate" label="생년월일"/>
                </DataTable>
            </TabsContent>
        </Tabs>
    </List>
);
export const UserShow = () => (
    <Show>
        <div className="flex flex-col gap-4">
            <RecordField source="id"/>
            <RecordField source="status"/>
            <RecordField source="roles"
                         render={(record) => (
                             <div style={{display: "flex", gap: "8px"}}>
                                 {record.roles?.map((role: any) => (
                                     <span key={role.roleId}
                                           style={{padding: "4px 8px", background: "#eee", borderRadius: "12px"}}>
                             {role.roleName}
                         </span>
                                 ))}
                             </div>
                         )}
            />
            <RecordField source="createdAt">
                <DateField source="createdAt" showTime/>
            </RecordField>
            <RecordField source="updatedAt">
                <DateField source="updatedAt" showTime/>
            </RecordField>
            <RecordField source="provider"/>
            <RecordField source="userId"/>
            <RecordField source="providerUserId" label="oauth2 Id">

            </RecordField>
            <RecordField source="email"/>
            <RecordField source="phone"/>
            <RecordField source="name"/>
            <RecordField source="nickname"/>
            <RecordField source="address"/>
            <RecordField source="addressDetail"/>
            <RecordField source="zipcode"/>
            <RecordField source="bio"/>
            <RecordField source="profileUrl"
                         render={(record) => (
                             (record.profileUrl && <img
                                     src={record.profileUrl}
                                     alt="프로필 이미지"
                                     style={{width: 40, height: 40, borderRadius: "50%"}}
                                 />
                             )
                         )}>

            </RecordField>
            <RecordField source="birthDate"/>

        </div>
    </Show>
);


const RoleSelectInput = (props: JSX.IntrinsicAttributes & ChoicesProps & Partial<InputProps> & Omit<SupportCreateSuggestionOptions<unknown>, "handleChange"> & { emptyText?: string | ReactElement; emptyValue?: any; onChange?: (value: string) => void; } & Omit<Omit<DetailedHTMLProps<HTMLAttributes<HTMLDivElement>, HTMLDivElement>, "id"> & { id: string; name: string; }, "id" | "children" | "name">) => {
    const { data, isLoading } = useGetList('roles'); // GET /roles 호출

    if (isLoading) return <span>Loading...</span>;

    return (
        <SelectInput
            {...props}
            source="roleId"
            label="Role"
            optionValue="roleId"
            optionText="roleName"
            choices={data}
        />

    );
};


export const UserEdit = () => {


  return(
    <Edit actions={<CustomEditActions resource="users" />} >
        <SimpleForm>

            <TextInput source="id" disabled />
            <SelectInput
                source="status"
                defaultValue='ACTIVE'
                choices={[
                    { id: 'ACTIVE', name: '가입중 (ACTIVE)' },
                    { id: 'INACTIVE', name: '회원탈퇴 (INACTIVE)' },
                    { id: 'BLOCKED', name: '블랙리스트 (BLOCKED)' },
                ]}
            />
            <ArrayInput source="roles">
                <SimpleFormIterator>
                    <RoleSelectInput/>

                </SimpleFormIterator>
            </ArrayInput>


            <TextInput source="provider" disabled />
            <TextInput source="userId" disabled />
            <TextInput source="providerUserId" disabled />

            <TextInput source="email" />
            <TextInput source="phone" validate={required()} />
            <TextInput source="name" />
            <TextInput source="nickname" validate={required()} />
            <TextInput source="address"  />
            <TextInput multiline source="addressDetail" />
            <TextInput source="zipcode" />
            <TextInput multiline  source="bio" />

            <FileUploadInput source="profileUrl" label="프로필 이미지" />
            <DateInput source="birthDate" />

            <TextInput source="roles" />

        </SimpleForm>
    </Edit>
)
}

