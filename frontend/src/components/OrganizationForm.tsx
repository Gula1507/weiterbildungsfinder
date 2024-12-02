import axios from "axios";
import {useEffect, useState} from "react";
import "../styles/OrganizationForm.css"
import {useLocation, useNavigate, useParams} from "react-router-dom";
import {Organization} from "../types/Organization";

function OrganizationForm() {
    const {id} = useParams(); // Wenn die ID über die URL-Parameter übergeben wird
    console.log('organizationId from URL:', id);
    const location = useLocation();
    const navigate = useNavigate();
    const [name, setName] = useState("");
    const [homepage, setHomepage] = useState("");
    const [email, setEmail] = useState("");
    const [address, setAddress] = useState("");
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');


    const organization1 = location.state?.organization;
    console.log('organization:', organization1);

    const organization: Organization | undefined = location.state?.organization;

    useEffect(() => {
        if (organization) {
            setName(organization.name);
            setHomepage(organization.homepage);
            setEmail(organization.email);
            setAddress(organization.address);
        }
    }, [organization])

    const handleSubmit = (event: React.FormEvent) => {
        event.preventDefault();
        const newOrganization = {name, homepage, email, address};

        const request = organization
            ? axios.put(`/api/organizations/${id}`, newOrganization)
            : axios.post('/api/organizations', newOrganization);

        request
            .then((response) => {
                setSuccessMessage('Die Organisation wurde erfolgreich gespeichert!');
                setErrorMessage('');
                navigate(`/organizations/${response.data.id}`);
            })
            .catch(() => {
                setErrorMessage('Fehler beim Speichern der Organisation.');
            });
    };


    return (
        <div className="new-organization">
            <h2>{organization ? 'Weiterbildungsanbieter bearbeiten' : 'Neuer Weiterbildungsanbieter'}</h2>
            <form onSubmit={handleSubmit} className="form">
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    placeholder={organization ? organization.name : "Name des Anbieters"}
                    required
                />
                <input
                    type="text"
                    value={homepage}
                    onChange={(e) => setHomepage(e.target.value)}
                    placeholder={organization ? organization.homepage : "Webseite"}
                />
                <input
                    type="text"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder={organization ? organization.email : "Email"}
                />
                <input
                    type="text"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    placeholder={organization ? organization.address : "Address"}
                />
                <button type="submit">Absenden</button>
            </form>
            {successMessage && <p className="success-message">{successMessage}</p>}
            {errorMessage && <p className="error-message">{errorMessage}</p>}
        </div>
    );
}

export default OrganizationForm;