import {FaRegStar, FaStar, FaStarHalfAlt} from "react-icons/fa";
import "../styles/StarsRating.css"

type StarsProps = {
    rating: number;
}

function StarsRating(props: StarsProps) {
    return (
        <div id="rating">
            {Array.from({length: 5}, (_, index) => {
                const starValue = index + 1;
                if (starValue <= Math.floor(props.rating)) {
                    return <FaStar key={index} className="star"/>;
                } else if (starValue === Math.ceil(props.rating) && !Number.isInteger(props.rating)) {
                    return <FaStarHalfAlt key={index} className="star"/>;
                } else {
                    return <FaRegStar key={index} className="star"/>;
                }
            })}
        </div>
    );
}

export default StarsRating;