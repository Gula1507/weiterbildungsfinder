import {Organization} from "../types/Organization.ts";
import "../styles/OrganizationCard.css"

type OrganizationPageProps = {
    organization: Organization
}

function OrganizationCard(props: OrganizationPageProps) {
    return (
        <div className="card">
            <h2 className="card-title">{props.organization.name}</h2>
            <a className="card-homepage" target="_blank"
               href={props.organization.homepage}>{props.organization.homepage}</a>
        </div>
    )
}

export default OrganizationCard;