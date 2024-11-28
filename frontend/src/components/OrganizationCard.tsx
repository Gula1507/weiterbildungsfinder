import {Organization} from "../types/Organization.ts";
import "../styles/OrganizationCard.css"
import {useNavigate} from 'react-router-dom';

type OrganizationPageProps = {
    organization: Organization
}

function OrganizationCard(props: OrganizationPageProps) {

    const navigate = useNavigate();

    function switchToDetailsPage() {
        navigate(`api/organizations/${props.organization.id}`);
    }

    return (
        <div className="card" onClick={switchToDetailsPage}>
            <h2 className="card-title">{props.organization.name}</h2>
        </div>
    )
}

export default OrganizationCard;