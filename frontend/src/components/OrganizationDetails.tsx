import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Organization} from "../types/Organization.ts";

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
            <h1>{organization?.name}</h1>
            <p>
                Webseite: <a
                href={organization?.homepage?.startsWith('http') ? organization?.homepage
                    : `http://${organization?.homepage}`}
                target="_blank"
                rel="noopener noreferrer"
            >
                {organization?.homepage}
            </a>
            </p>
            <p>Email: {organization?.email}</p>
            <p>Adresse: {organization?.address}</p>
            <button onClick={() => navigate(`/edit-organization/${id}`, {state: {organization}})}
                    className="edit-button">
                Bearbeiten
            </button>
            <button onClick={deleteOrganization} className="delete-button">Löschen</button>
            {deleteSuccess && (
                <p className="success-message">Der Anbieter wurde gelöscht! Weiterleitung auf die Startseite...</p>
            )}
        </div>
    );
}

export default OrganizationDetails;