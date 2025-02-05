import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Organization} from "../types/Organization.ts";
import "../styles/OrganizationDetails.css"
import StarsRating from "./StarsRating.tsx";
import {AppUser} from "../types/AppUser.ts";


type OrganizationDetailsProps = {
    appUser: AppUser|null|undefined;
    onLogin: (user: AppUser) => void;
}


function OrganizationDetails(props:OrganizationDetailsProps) {
    const {id} = useParams();
    const navigate = useNavigate();
    const [organization, setOrganization] = useState<Organization | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [deleteSuccess, setDeleteSuccess] = useState(false);
    const [expandedCourseId, setExpandedCourseId] = useState<string | null>(null);

    useEffect(() => {

        async function fetchOrganizationDetails() {
            try {
                const response = await axios.get<Organization>(`/api/organizations/${id}`);
                setOrganization(response.data);
                setLoading(false);
            } catch (err) {
                setError((err as Error).message || 'An unknown error occurred');
                setLoading(false);
            }
        }

        fetchOrganizationDetails();
    }, [id]);

    if (loading) return <p>Loading organization details...</p>;

    if (error) return <p>Error loading organization: {error}</p>;

    function deleteOrganization() {
        const userConfirmed = window.confirm("Sind Sie sicher, dass Sie diesen Anbieter löschen möchten?");
        if (!userConfirmed) {
            return;
        }
        axios.delete(`/api/organizations/${id}`)
            .then(() => {
                setDeleteSuccess(true);
                setTimeout(() => {
                    setDeleteSuccess(false);
                    navigate('/');
                    window.location.reload();
                }, 3000);
            })
            .catch((err) => {
                setError((err as Error).message || 'An error occurred while deleting the organization');
            });
    }


    const toggleCourseDetails = (courseId: string) => {

        setExpandedCourseId(prevId => (prevId === courseId ? null : courseId));
    };
    return (
        <div>

            <button
                className="review-button"
                onClick={() => {
                    if (!props.appUser) {

                        navigate('/login', {state: {from: `/add-review/${id}`, organization}});

                    } else {

                        navigate(`/add-review/${id}`, {state: {organization}});
                    }
                }}
            >
                Bewerten
            </button>

            {props.appUser?.appUserRole === "ADMIN" &&
                <button onClick={() => navigate(`/edit-organization/${id}`, {state: {organization}})}>
                    Bearbeiten
                </button>}
            {props.appUser?.appUserRole === "ADMIN" &&
                <button onClick={deleteOrganization} className="delete-button">Löschen</button>}
            {deleteSuccess && (
                <p className="success-message">Der Anbieter wurde gelöscht! Weiterleitung auf die Startseite...</p>
            )}
            <h2>{organization?.name}</h2>

            <p>
                <br/>
                <strong>Webseite:</strong> <a
                href={organization?.homepage?.startsWith('http') ? organization?.homepage
                    : `http://${organization?.homepage}`}
                target="_blank"
                rel="noopener noreferrer"
            >
                {organization?.homepage}
            </a>
            </p>

            <p><strong>Email:</strong> {organization?.email}</p>
            <p><strong>Adresse:</strong> {organization?.address}</p>


            <div className="spacing">
                <strong>Durchschnitsnote: </strong>
                {(organization?.averageRating === null || organization?.averageRating === 0.0)
                    ? "noch keine Bewertung"

                    : (<StarsRating rating={parseFloat(organization?.averageRating.toFixed(1) as string)}/>)}
            </div>

            <div className="spacing"><strong>Rezensionen: </strong> {organization?.reviews && organization.reviews.length > 0
                ?
                (<ul className="review-list">
                    {organization?.reviews.map((r) => (
                        <li key={r.id} className="review-item">{r.author}: {r.comment} <br/>
                            <StarsRating rating={r.starNumber}/>
                        </li>))}
                </ul>)
                : "noch keine Rezensionen vorhanden"}</div>
            <div className="spacing">
                <strong> Kurse: </strong>
                {organization?.courses && organization.courses.length > 0 ? (
                    <ul className="course-list">
                        {organization.courses.map((course) => (
                            <li key={course.id} className="course-item">
                                <div
                                    role="button"
                                    onClick={() => toggleCourseDetails(course.id)}
                                    onKeyDown={(e) => {
                                        if (e.key === 'Enter' || e.key === ' ') {
                                            e.preventDefault();
                                            toggleCourseDetails(course.id);
                                        }
                                    }}
                                    tabIndex={0}
                                    style={{cursor: 'pointer'}}
                                >
                                    {course.courseName}
                                </div>

                                {expandedCourseId === course.id && (
                                    <div className="course-details">
                                        <p className="title-course-details"><strong>Inhalt:</strong></p>
                                        <div
                                            dangerouslySetInnerHTML={{__html: course.courseContent}}
                                            className="course-element"
                                        />
                                        <p className="title-course-details"><strong>Abschluss:</strong></p>
                                        <div
                                            dangerouslySetInnerHTML={{__html: course.courseDegree}}
                                            className="course-element"
                                        />
                                        <p className="title-course-details">
                                            <strong>Förderung:</strong></p>
                                        {course.educationVoucher ? (
                                                <div
                                                    dangerouslySetInnerHTML={{__html: course.educationVoucher}}
                                                    className="course-element"
                                                />)
                                            : (<div className="course-element">Keine Informationen</div>)}

                                    </div>
                                )}
                            </li>
                        ))}
                    </ul>
                ) : (
                    "Keine Kurse vorhanden"
                )}
            </div>
        </div>
    );
}

export default OrganizationDetails;