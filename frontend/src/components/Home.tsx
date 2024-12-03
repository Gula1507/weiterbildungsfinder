import {Organization} from "../types/Organization.ts";
import OrganizationGallery from "./OrganizationGallery.tsx";
import "../styles/Home.css"
import {useNavigate} from "react-router-dom";
import {useState} from "react";

type HomeProps = {
    organizations: Organization[]
}

function Home(props: HomeProps) {

    const navigate = useNavigate();

    const [searchText, setSearchText] = useState("");

    const handleAddOrganization = () => {
        navigate("add-organization")
    }

    const filteredOrganizations: Organization[] = props.organizations
        .filter((organization) => organization.name.toLowerCase().includes(searchText.toLowerCase()));

    return (
        <>
            <button onClick={handleAddOrganization}>Weiterbildungsanbieter hinzufügen</button>
            <div className="home-container">
                <input
                    type="text"
                    onChange={(e) => setSearchText(e.target.value)}
                    placeholder="Nach einem Weiterbildungsanbieter suchen"
                />

                {filteredOrganizations.length > 0 ? (
                    <OrganizationGallery organizations={filteredOrganizations}/>
                ) : (
                    "Keine Weiterbildungsanbieter gefunden"
                )}
                <button className="add" onClick={handleAddOrganization}>
                    + NEU
                </button>
            </div>
        </>
    );
}

export default Home;