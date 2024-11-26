import {Organization} from "../types/Organization.ts";
import OrganizationGallery from "./OrganizationGallery.tsx";
import "../styles/Home.css"

type HomeProps = {
    organizations: Organization[]
}

function Home(props: HomeProps) {
    return (
        <div className="home-container">
            <OrganizationGallery organizations={props.organizations}/>
        </div>
    );
}

export default Home;