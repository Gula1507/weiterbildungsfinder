import {Review} from "./Review.ts";

export type Organization =
    {
        id: string;
        name: string;
        homepage: string;
        email: string;
        address: string;
        reviews: Review[];
        averageRating: number;
    }