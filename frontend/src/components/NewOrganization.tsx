import axios from "axios";
import {useState} from "react";
import "../styles/NewOrganization.css"

function NewOrganization() {
    const [name, setName] = useState("");
    const [homepage, setHomepage] = useState("");
    const [email, setEmail] = useState("");
    const [address, setAddress] = useState("");
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');


    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        const newOrganization = {name, homepage, email, address};

        axios
            .post('api/organizations', newOrganization)
            .then((response) => {
                console.log('Neue Organisation erstellt:', response.data);
                setName('');
                setSuccessMessage('Die Organisation wurde erfolgreich hinzugefügt!');
                setErrorMessage('');
            })
            .catch((error) => {
                console.error('Fehler beim Erstellen der Organisation:', error);
                setErrorMessage('Fehler beim Hinzufügen der Organisation. Bitte versuche es später erneut.');
                setSuccessMessage('');
            });
    };

    return (
        <div className="new-organization">
            <h2>Neuer Weiterbildungsanbieter</h2>
            <form onSubmit={handleSubmit} className="form">
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder="Name der Organisation"
                    required
                />
                <input
                    type="text"
                    value={homepage}
                    onChange={(e) => setHomepage(e.target.value)}
                    placeholder="Webseite"
                />
                <input
                    type="text"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="Email"
                />
                <input
                    type="text"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    placeholder="Address"
                />
                <button type="submit">Absenden</button>
            </form>
            {successMessage && <p className="success-message">{successMessage}</p>}
            {errorMessage && <p className="error-message">{errorMessage}</p>}
        </div>
    );
}

export default NewOrganization;