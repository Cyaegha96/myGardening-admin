import { type DataProvider } from "ra-core";

import axiosInterceptor from "@/shared/api/axiosInterceptor";


export const dataProvider: DataProvider = {
    // ----------------------------------------------------
    // 1) 리스트 조회 (GET /resource?sort=&range=&filter=)
    // ----------------------------------------------------
    getList: async (resource, params) => {
        const page = params.pagination?.page ?? 1;
        const perPage = params.pagination?.perPage ?? 10;

        const { field, order } = params.sort || { field: 'createdAt', order: 'ASC' };

        const url =
            `/${resource}?sort=${encodeURIComponent(JSON.stringify({ field, order }))}` +
            `&range=${encodeURIComponent(JSON.stringify([(page - 1) * perPage, page * perPage - 1]))}` +
            `&filter=${encodeURIComponent(JSON.stringify(params.filter ?? {}))}`;

        const res = await axiosInterceptor.get(url);

        const data = res.data.map((item: any) => ({
            ...item,
        }));
        const contentRange =
            res.headers["content-range"] ||
            res.headers["Content-Range"] ||
            "users 0-0/0";

        const total = Number(contentRange.split("/").pop() ?? 0);

        return {
            data,
            total,
        };
    },


    // ----------------------------------------------------
    // 2) 단건 조회 (GET /resource/:id)
    // ----------------------------------------------------
    getOne: async (resource, params) => {
        const res = await axiosInterceptor.get(`/${resource}/${params.id}`);
        const item = res.data; // 단일 객체
        const data = {
            ...item,


        };


        return { data };


    },

    // ----------------------------------------------------
// 3) 여러개 조회 (GET /resource?filter={id:[]})
// ----------------------------------------------------
    getMany: async (resource, params) => {
        const url = `/${resource}?filter=${encodeURIComponent(
            JSON.stringify({ id: params.ids })
        )}`;

        const res = await axiosInterceptor.get(url);
        return { data: res.data };
    },




    // ----------------------------------------------------
    // 4) 연관된 목록 조회
    // ----------------------------------------------------
    getManyReference: async (resource, params) => {
        const page = params.pagination?.page ?? 1;
        const perPage = params.pagination?.perPage ?? 10;
        const { field, order } = params.sort || { field: 'createdAt', order: 'ASC' };

        const url =
            `/${resource}?sort=${encodeURIComponent(JSON.stringify([field, order]))}` +
            `&range=${encodeURIComponent(JSON.stringify([(page - 1) * perPage, page * perPage - 1]))}` +
            `&filter=${encodeURIComponent(JSON.stringify({
                ...params.filter,
                [params.target]: params.id,
            }))}`;

        const res = await axiosInterceptor.get(url);

        const data = res.data.map((item: any) => ({
            ...item,
        }));

        const contentRange =
            res.headers["content-range"] ||
            res.headers["Content-Range"] ||
            "users 0-0/0";

        const total = Number(contentRange.split("/").pop() ?? 0);

        return {
            data,
            total,
        };
    },

    // ----------------------------------------------------
    // 5) update (PUT /resource/:id)
    // ----------------------------------------------------
    update: async (resource, params) => {
        const res = await axiosInterceptor.put(
            `${resource}/${params.id}`,
            params.data
        );
        return { data: res.data };
    },


// ----------------------------------------------------
// 6) 여러개 update (PUT /resource?filter={id:[]})
// ----------------------------------------------------
    updateMany: async (resource, params) => {
        const query = {
            filter: JSON.stringify({ id: params.ids }),
        };
        const url = `/${resource}?${encodeURIComponent(JSON.stringify(query))}`;

        const res = await axiosInterceptor.put(url, params.data);

        const data = res.data.map((item: any) => ({
            ...item,
        }));

        return { data };
    },

// ----------------------------------------------------
// 7) create (POST /resource)
// ----------------------------------------------------
    create: async (resource, params) => {
        const url = `/${resource}`;
        const res = await axiosInterceptor.post(url, params.data);

        const data = { ...res.data };

        return { data };
    },

// ----------------------------------------------------
// 8) delete (DELETE /resource/:id)
// ----------------------------------------------------
    delete: async (resource, params) => {
        const url = `/${resource}/${params.id}`;
        const res = await axiosInterceptor.delete(url);

        const data = { ...res.data };

        return { data };
    },

// ----------------------------------------------------
// 9) 여러개 delete (DELETE /resource?filter={id:[]})
// ----------------------------------------------------
    deleteMany: async (resource, params) => {
        const url = `/${resource}?filter=${encodeURIComponent(
            JSON.stringify({ id: params.ids })
        )}`;

        const res = await axiosInterceptor.delete(url);

        return { data: res.data };
    },

};
