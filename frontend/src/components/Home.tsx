import {Organization} from "../types/Organization.ts";
import OrganizationGallery from "./OrganizationGallery.tsx";

type HomeProps = {
    organizations: Organization[]
}

function Home(props: HomeProps) {
    return (
        <div>
            <OrganizationGallery organizations={props.organizations}/>
        </div>
    );
}

export default Home;