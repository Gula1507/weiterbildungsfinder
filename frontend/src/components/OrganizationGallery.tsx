import {Organization} from "../types/Organization.ts";
import OrganizationCard from "./OrganizationCard.tsx";
import "../styles/OrganizationGallery.css"

type OrganizationProps = {
    organizations: Organization[]
}

function OrganizationGallery(props: OrganizationProps) {
    return (
        <div className="gallery-container">
            {props.organizations.map((organization) => (
                <OrganizationCard organization={organization} key={organization.id}/>))
            }
        </div>)
}

export default OrganizationGallery;