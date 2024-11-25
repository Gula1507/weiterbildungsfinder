import {Organization} from "../types/Organization.ts";
import OrganizationCard from "./OrganizationCard.tsx";

type OrganizationProps = {
    organizations: Organization[]
}

function OrganizationGallery(props: OrganizationProps) {
    return (
        <div className="organization-gallery">
            {props.organizations.map((organization) => (
                <OrganizationCard organization={organization} key={organization.id}/>))
            }
        </div>)
}

export default OrganizationGallery;