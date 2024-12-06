import {Organization} from "../types/Organization.ts";
import OrganizationGallery from "./OrganizationGallery.tsx";
import "../styles/Home.css"
import {useNavigate} from "react-router-dom";
import {useState} from "react";

type HomeProps = {
    organizations: Organization[]
    loading: boolean

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

            <div className="slogan-container">
                <img
                    className="small-image"
                    src="/images/pictures.png"
                    alt="Suchender Geschäftsmann auf dem Schiff"
                />
                <div className="slogan">
                    <h2>Entdecke KURSDA</h2>
                    <p>
                        Finde heraus, welche Weiterbildungsmöglichkeiten es in deiner Region gibt – übersichtlich und
                        einfach.
                        Hol dir neue Fähigkeiten, bleib motiviert und gestalte deine Zukunft aktiv – jetzt und für die
                        kommenden Jahre.
                    </p>
                </div>
            </div>

            <div className="home-container">
                {props.loading ? (
                    <div className="loading-indicator">Lade Weiterbildungsanbieter...</div>
                ) : (
                    <>
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
                    </>
                )}
            </div>
        </>
    );
}

export default Home;