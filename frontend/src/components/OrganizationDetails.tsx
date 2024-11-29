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
            <button onClick={() => navigate(`/edit-organization/${id}`, {state: {organization}})}>
                Bearbeiten
            </button>
        </div>
    );
}

export default OrganizationDetails;
