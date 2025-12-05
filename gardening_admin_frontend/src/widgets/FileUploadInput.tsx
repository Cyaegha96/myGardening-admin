import { useInput } from "ra-core";
import { useState, useCallback } from "react";
import { useDropzone } from "react-dropzone";
import axiosInterceptor from "@/shared/api/axiosInterceptor";

interface FileUploadInputProps {
    source: string;
    label?: string;
}

export const FileUploadInput = ({ source }: FileUploadInputProps) => {
    const {
        field,
        fieldState: { error },
    } = useInput({ source });

    const [preview, setPreview] = useState<string | null>(field.value || null);
    const [uploading, setUploading] = useState(false);

    const onDrop = useCallback(async (acceptedFiles: File[]) => {
        if (!acceptedFiles.length) return;

        const file = acceptedFiles[0];
        const formData = new FormData();
        formData.append("file", file);
        formData.append("folder", "plant/images/");

        try {
            setUploading(true);
            const res = await axiosInterceptor.post("/file/upload", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });

            const uploadedUrl = res.data.url;
            field.onChange(uploadedUrl);
            setPreview(uploadedUrl);
        } catch (err) {
            console.error(err);
        } finally {
            setUploading(false);
        }
    }, [field]);

    const { getRootProps, getInputProps, isDragActive } = useDropzone({
        onDrop,
        accept: { "image/*": [] },
        multiple: false,
    });

    return (
        <div className="mb-4">

            <div
                {...getRootProps()}
                className={`flex items-center justify-center border-2 border-dashed rounded p-4 cursor-pointer transition-colors
                    ${isDragActive ? "border-blue-500 bg-blue-50" : "border-gray-300 bg-gray-50"}
                `}
            >
                <input {...getInputProps()} />

                {preview ? (
                    <img
                        src={preview}
                        alt="preview"
                        className="w-32 h-32 object-cover rounded border"
                    />
                ) : uploading ? (
                    <p>업로드 중...</p>
                ) : isDragActive ? (
                    <p>여기에 파일을 놓으세요</p>
                ) : (
                    <p>이미지 파일을 드래그하거나 클릭하여 선택하세요</p>
                )}
            </div>

            {error && <p className="text-red-500 text-sm mt-1">{error.message}</p>}
        </div>
    );
};
