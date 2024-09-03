import React, { useContext } from "react";
import { List, Card, Row, Col, Typography, Divider } from "antd";
import FlightResultsContext from "../../contexts/FlightResultsContext";
import { useNavigate } from "react-router-dom";
import "./index.css";
import dayjs from "dayjs";

const { Title, Paragraph} = Typography;

const Results: React.FC = () => {
    const { flightResults } = useContext(FlightResultsContext);
    const {setSelectedFlight} = useContext(FlightResultsContext);
    const navigate = useNavigate();

    const handleClick = (flightOffer : any) =>{
        setSelectedFlight(flightOffer);
        console.log(flightOffer);
        navigate('/details');
    };

    const parseDuration = (duration: any) => {
        const match = duration.match(/PT(\d+H)?(\d+M)?/);
        const hours = match[1] ? parseInt(match[1].replace('H', '')) : 0;
        const minutes = match[2] ? parseInt(match[2].replace('M', '')) : 0;
        return `${hours}h ${minutes}m`;
    };

    return (
        <div className="resultsContainer">
            <List
                dataSource={flightResults.data}
                renderItem={(offer: any) => (
                    <List.Item onClick={()=>handleClick(offer)} key={offer.id} className="clickable">
                        {offer.itineraries.map((itinerary: any) => (
                            <React.Fragment key={itinerary.id}>
                                <Row>
                                    <Col>
                                        {/*<p>Total time: {dayjs(itinerary.duration).format('HH:mm')}</p>*/}
                                        {itinerary.segments.map((segment: any) => (
                                            <Card key={segment.id}>
                                                <Row>
                                                    <Paragraph>{dayjs(segment.departure.at).format('YYYY-MM-DD HH:mm')} - {dayjs(segment.arrival.at).format('YYYY-MM-DD HH:mm')}</Paragraph>
                                                </Row>
                                                <Row>
                                                    <Col><p>{segment.departure.airportCommonName}({segment.departure.iataCode}) - {segment.arrival.airportCommonName}({segment.arrival.iataCode})</p></Col>
                                                    <Divider type="vertical" />
                                                    <Col><p>{parseDuration(segment.duration)}</p></Col>
                                                </Row>
                                            </Card>
                                        ))}
                                    </Col>
                                    <Divider type="vertical" />
                                    <Col>
                                        <Row>
                                            <p>$ {offer.price.total} {offer.price.currency} Total</p>
                                        </Row>
                                        <Row>
                                            <ul>
                                                {offer.travelerPricings.map((price: any) => (
                                                    <li key={price.travelerId}>
                                                        {price.travelerType} {price.travelerId}: $ {price.price.total} {price.price.currency}
                                                    </li>
                                                ))}
                                            </ul>
                                        </Row>
                                    </Col>
                                </Row>
                            </React.Fragment>
                        ))}
                    </List.Item>
                )}
            />
        </div>
    );
};

export default Results;
