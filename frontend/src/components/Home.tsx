import {Organization} from "../types/Organization.ts";
import OrganizationGallery from "./OrganizationGallery.tsx";
import "../styles/Home.css"
import {useNavigate} from "react-router-dom";

type HomeProps = {
    organizations: Organization[]
}

function Home(props: HomeProps) {

    const navigate = useNavigate();

    const handleAddOrganization = () => {
        navigate("add-organization")
    }
    return (
        <>
            <button onClick={handleAddOrganization}>Weiterbildungsanbieter hinzufügen</button>
            <div className="home-container">
                <OrganizationGallery organizations={props.organizations}/>
            </div>
        </>
    );
}

export default Home;