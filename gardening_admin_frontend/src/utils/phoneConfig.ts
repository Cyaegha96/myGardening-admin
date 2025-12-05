import {parsePhoneNumberFromString} from "libphonenumber-js";

export function toLocalPhone(phone: string): string {
    const parsed = parsePhoneNumberFromString(phone, "KR");
    return parsed ? parsed.formatNational() : phone;
}


export function toInternationalPhone(phone: string): string {
    const parsed = parsePhoneNumberFromString(phone, "KR");
    if (!parsed) return phone;

    const formatted = parsed.formatInternational(); // "+82 10 1234 5678"

    // 첫 번째 공백은 유지하고, 이후 공백은 하이픈으로 치환
    return formatted.replace(" ", " ").replace(/\s/g, "-").replace("+82-", "+82 ");
}