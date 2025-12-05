import {useEffect, useState} from "react";
import {Form, required, useLogin, useNotify} from "ra-core";
import type { SubmitHandler, FieldValues } from "react-hook-form";
import { Button } from "@/shared/shadcn/components/ui/button.tsx";
import { TextInput } from "@/shared/shadcn/components/admin/text-input.tsx";
import { Notification } from "@/shared/shadcn/components/admin/notification.tsx";
import { useNavigate} from "react-router";

export const LoginPage = (props: { redirectTo?: string }) => {
    const navigate = useNavigate();
    const { redirectTo } = props;

    useEffect(() => {
        const token = localStorage.getItem("accessToken");
        if (token) {
            navigate(redirectTo ?? "/", { replace: true });
        }
    }, [navigate, redirectTo]);
    const [loading, setLoading] = useState(false);
    const login = useLogin();
    const notify = useNotify();

    const handleSubmit: SubmitHandler<FieldValues> = (values) => {
        setLoading(true);
        login(values, redirectTo)
            .then(() => {
                setLoading(false);
            })
            .catch((error) => {
                setLoading(false);
                notify(
                    typeof error === "string"
                        ? error
                        : typeof error === "undefined" || !error.message
                            ? "ra.auth.sign_in_error"
                            : error.message,
                    {
                        type: "error",
                        messageArgs: {
                            _:
                                typeof error === "string"
                                    ? error
                                    : error && error.message
                                        ? error.message
                                        : undefined,
                        },
                    },
                );
            });
    };

    return (
        <div className="min-h-screen flex">
            <div className="container relative grid flex-col items-center justify-center sm:max-w-none lg:grid-cols-2 lg:px-0">
                <div className="relative hidden h-full flex-col bg-muted p-10 text-white dark:border-r lg:flex">
                    <div
                        className="absolute inset-0 bg-cover bg-center"
                        style={{
                            backgroundImage: "url('/src/shared/assets/background/login-bg.jpg')",
                        }}
                    >
                        {/* 바네팅 오버레이 */}
                        <div className="absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-black/60" />
                    </div>
                    <div className="relative z-20 flex items-center text-lg font-medium">
                        <img src="src/shared/assets/logo/myGardening.svg" className="mr-2 mt-2 h-8 w-8"  alt="로고 이미지"/>
                        <img src="src/shared/assets/logo/myGardeningText.png"  className="mr-2 h-8 " alt="로고 텍스트" />
                    </div>
                    <div className="relative z-20 mt-auto">
                        <blockquote className="space-y-2">
                            <p className="text-lg shadow-md ">
                                &ldquo;마이가드닝은 (주) 끼릭끼릭의 프로젝트로 AI를 통합하여 제공하는 사용자 친화적인 식집사 통합 커뮤니티입니다.&rdquo;
                            </p>
                            <footer className="text-sm">-끼릭끼릭</footer>
                        </blockquote>
                    </div>
                </div>
                <div className="lg:p-8">
                    <div className="mx-auto flex w-full flex-col justify-center space-y-6 sm:w-[350px]">
                        <div className="flex flex-col space-y-2 text-center">
                            <h1 className="text-2xl font-semibold tracking-tight">마이가드닝 관리자페이지</h1>
                            <p className="text-sm leading-none text-muted-foreground">
                                관리자 id/pw만 입력이 가능합니다.
                            </p>
                        </div>
                        <Form className="space-y-8" onSubmit={handleSubmit}>
                            <TextInput
                                label="아이디"
                                source="id"
                                type="text"
                                validate={required()}
                            />
                            <TextInput
                                label="비밀번호"
                                source="password"
                                type="password"
                                validate={required()}
                            />
                            <Button
                                type="submit"
                                className="cursor-pointer"
                                disabled={loading}
                            >
                               로그인
                            </Button>
                        </Form>
                    </div>
                </div>
            </div>
            <Notification />
        </div>
    );
};
