import {Organization} from "../types/Organization.ts";
import OrganizationCard from "./OrganizationCard.tsx";

type HomeProps = {
    organizations: Organization[]
}

function Home(props: HomeProps) {
    return (
        <div>
            {props.organizations.map((organization) => (
                <OrganizationCard organization={organization} key={organization.id}/>))
            }
        </div>)
}

export default Home;