import {Organization} from "../types/Organization.ts";
import OrganizationGallery from "./OrganizationGallery.tsx";
import "../styles/Home.css"
import AddOrganization from "./AddOrganization.tsx";

type HomeProps = {
    organizations: Organization[]
}

function Home(props: HomeProps) {
    return (
        <>
            <AddOrganization/>
        <div className="home-container">
            <OrganizationGallery organizations={props.organizations}/>
        </div>
        </>
    );
}

export default Home;