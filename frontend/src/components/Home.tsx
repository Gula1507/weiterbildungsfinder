import {Organization} from "../types/Organization.ts";
import OrganizationGallery from "./OrganizationGallery.tsx";
import "../styles/Home.css"
import {useNavigate} from "react-router-dom";
import {useState} from "react";

type HomeProps = {
    organizations: Organization[]
    loading: boolean
    loadOrganizations: (page: number, size: number) => void;
    totalPages: number;
}

function Home(props: HomeProps) {
    const navigate = useNavigate();

    const [searchText, setSearchText] = useState("");
    const [page, setPage] = useState<number>(0);
    const [size] = useState<number>(10);


    const handleAddOrganization = () => {
        navigate("add-organization")
    }

    const filteredOrganizations: Organization[] = props.organizations
        .filter((organization) => organization.name.toLowerCase().includes(searchText.toLowerCase()));

    const handlePageChange = (newPage: number) => {
        if (newPage >= 0 && newPage < props.totalPages) {
            setPage(newPage);
            props.loadOrganizations(newPage, size);
        }
    };

    return (
        <>

            <div className="slogan-container">
                <img
                    className="small-image"
                    src="/images/home-icons.png"
                    alt="Icons representing home features such as house, magnifying glass, thumbs up"
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

                <div className="pagination">
                    <button onClick={() => handlePageChange(page - 1)} disabled={page <= 0}>🢦</button>
                    <span>Seite {page + 1} von {props.totalPages}</span>
                    <button onClick={() => handlePageChange(page + 1)} disabled={page >= props.totalPages - 1}>
                        🢧
                    </button>
                </div>
            </div>
        </>
    );
}

export default Home;