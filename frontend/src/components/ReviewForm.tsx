import "../styles/ReviewForm.css"
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import {useState} from "react";
import StarsRating from "./StarsRating.tsx";

function ReviewForm() {
    const {id} = useParams();
    // const location = useLocation();
    const [author, setAuthor] = useState("");
    const [starNumber, setStarNumber] = useState(0);
    const [comment, setComment] = useState("");
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        const newReview = {author, starNumber, comment};

        const request =
            axios.post(`/api/organizations/${id}/reviews`, newReview);

        request
            .then(() => {
                setSuccessMessage('Deine Bewertung wurde erfolgreich gespeichert!');
                setErrorMessage('');
                navigate(`/organizations/${id}`);
            })
            .catch(() => {
                setErrorMessage('Fehler beim Speichern der Bewertung.');
            });
    };
    const handleStarClick = (value: number) => {
        setStarNumber(value);
    };
    return (
        <div className="new-review">
            <h2>Neue Rezension</h2>
            <form onSubmit={handleSubmit} className="form">
                <input
                    type="text"
                    value={author}
                    onChange={(e) => setAuthor(e.target.value)}
                    placeholder={"Dein Name"}
                    required
                />
                <div id="rating-buttons">
                    {[1, 2, 3, 4, 5].map((value) => (
                        <button
                            key={value}
                            type="button"
                            onClick={() => handleStarClick(value)}
                            className="rating-button"
                        >
                            <StarsRating rating={value}/>
                        </button>
                    ))}
                </div>
                <div>Anzahl Sterne: {starNumber || "noch keine"}</div>
                <textarea
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    placeholder={"Dein Kommentar"}
                />

                <button type="submit">Absenden</button>
            </form>
            {successMessage && <p className="success-message">{successMessage}</p>}
            {errorMessage && <p className="error-message">{errorMessage}</p>}
        </div>
    );
}

export default ReviewForm;