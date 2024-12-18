import {CourseType} from "./CourseType.ts";

export type Course= {
    courseId: number,
    courseName:string,
    courseContent:string,
    courseDegree:string,
    educationVoucher:string,
    courseType: CourseType,
    apiOrganizationId: number;
}