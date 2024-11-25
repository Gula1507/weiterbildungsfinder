import {Organization} from "../types/Organization.ts";

type OrganizationPageProps = {
    organization: Organization
}

function OrganizationCard(props: OrganizationPageProps) {
    return (
        <div>
            <p>{props.organization.name}</p>
            <p>{props.organization.homepage}</p>

        </div>
    )
        ;
}

export default OrganizationCard;