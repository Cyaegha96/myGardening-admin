import type { ErrorInfo } from "react";
import { Suspense, useState } from "react";
import { cn } from "@/shared/shadcn/lib/utils";
import type { CoreLayoutProps } from "ra-core";
import { ErrorBoundary } from "react-error-boundary";
import { SidebarProvider, SidebarTrigger } from "@/shared/shadcn/components/ui/sidebar";
import { UserMenu } from "@/shared/shadcn/components/admin/user-menu";
import { ThemeModeToggle } from "@/shared/shadcn/components/admin/theme-mode-toggle";
import { Notification } from "@/shared/shadcn/components/admin/notification";
import { AppSidebar } from "@/shared/shadcn/components/admin/app-sidebar";
import { RefreshButton } from "@/shared/shadcn/components/admin/refresh-button";
import { LocalesMenuButton } from "@/shared/shadcn/components/admin/locales-menu-button";
import { Error } from "@/shared/shadcn/components/admin/error";
import { Loading } from "@/shared/shadcn/components/admin/loading";

export const Layout = (props: CoreLayoutProps) => {
  const [errorInfo, setErrorInfo] = useState<ErrorInfo | undefined>(undefined);
  const handleError = (_: Error, info: ErrorInfo) => {
    setErrorInfo(info);
  };
  return (
    <SidebarProvider>
      <AppSidebar />
      <main
        className={cn(
          "ml-auto w-full max-w-full",
          "peer-data-[state=collapsed]:w-[calc(100%-var(--sidebar-width-icon)-1rem)]",
          "peer-data-[state=expanded]:w-[calc(100%-var(--sidebar-width))]",
          "sm:transition-[width] sm:duration-200 sm:ease-linear",
          "flex h-svh flex-col",
          "group-data-[scroll-locked=1]/body:h-full",
          "has-[main.fixed-main]:group-data-[scroll-locked=1]/body:h-svh",
        )}
      >
        <header className="flex h-16 md:h-12 shrink-0 items-center gap-2 px-4">
          <SidebarTrigger className="scale-125 sm:scale-100" />
          <div className="flex-1 flex items-center" id="breadcrumb" />
          <LocalesMenuButton />
          <ThemeModeToggle />
          <RefreshButton />
          <UserMenu />
        </header>
        <ErrorBoundary
          onError={handleError}
          fallbackRender={({ error, resetErrorBoundary }) => (
            <Error
              error={error}
              errorInfo={errorInfo}
              resetErrorBoundary={resetErrorBoundary}
            />
          )}
        >
          <Suspense fallback={<Loading />}>
            <div className="flex flex-1 flex-col px-4 ">{props.children}</div>
          </Suspense>
        </ErrorBoundary>
      </main>
      <Notification />
    </SidebarProvider>
  );
};
