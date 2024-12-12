import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Organization} from "../types/Organization.ts";
import "../styles/OrganizationDetails.css"

function OrganizationDetails() {
    const {id} = useParams();
    const navigate = useNavigate();
    const [organization, setOrganization] = useState<Organization | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [deleteSuccess, setDeleteSuccess] = useState(false);

    useEffect(() => {

        async function fetchOrganizationDetails() {
            try {
                const response = await axios.get<Organization>(`/api/organizations/${id}`);
                setOrganization(response.data);
                setLoading(false);
            } catch (err) {
                setError((err as Error).message || 'An unknown error occurred');
                setLoading(false);
            }
        }

        fetchOrganizationDetails();
    }, [id]);


    if (loading) return <p>Loading organization details...</p>;


    if (error) return <p>Error loading organization: {error}</p>;

    function deleteOrganization() {
        const userConfirmed = window.confirm("Sind Sie sicher, dass Sie diesen Anbieter löschen möchten?");
        if (!userConfirmed) {
            return;
        }
        axios.delete(`/api/organizations/${id}`)
            .then(() => {
                setDeleteSuccess(true);
                setTimeout(() => {
                    setDeleteSuccess(false);
                    navigate('/');
                    window.location.reload();
                }, 3000);
            })
            .catch((err) => {
                setError((err as Error).message || 'An error occurred while deleting the organization');
            });
    }

    return (
        <div>
            <h2>{organization?.name}</h2>
            <button className="review-button" onClick={() => navigate(`/add-review/${id}`,
                {state: {organization}})}>
                Bewerten
            </button>
            <button onClick={() => navigate(`/edit-organization/${id}`, {state: {organization}})}>
                Bearbeiten
            </button>
            <button onClick={deleteOrganization} className="delete-button">Löschen</button>
            {deleteSuccess && (
                <p className="success-message">Der Anbieter wurde gelöscht! Weiterleitung auf die Startseite...</p>
            )}
            <p>
                <br/>
                <strong>Webseite:</strong> <a
                href={organization?.homepage?.startsWith('http') ? organization?.homepage
                    : `http://${organization?.homepage}`}
                target="_blank"
                rel="noopener noreferrer"
            >
                {organization?.homepage}
            </a>
            </p>

            <p><strong>Email:</strong> {organization?.email}</p>
            <p><strong>Adresse:</strong> {organization?.address}</p>

            <br/>
            <p>
                <strong>Durchschnitsnote</strong>: {(organization?.averageRating === null || organization?.averageRating === 0.0)
                ? "noch keine Bewertung"
                : organization?.averageRating.toFixed(1)}</p>

            <p><strong>Rezensionen</strong> {organization?.reviews && organization.reviews.length > 0
                ?
                (<ul className="review-list">
                    {organization?.reviews.map((r, index) => (
                        <li key={index} className="review-item">{r.author}: {r.comment} <br/>
                            <div className="stars">
                                {"★".repeat(r.starNumber) + "☆".repeat(5 - r.starNumber)}
                            </div>
                        </li>))}
                </ul>)
                : "noch keine Rezensionen vorhanden"}</p>
        </div>
    );
}

export default OrganizationDetails;