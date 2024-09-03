import React, { useContext } from "react";
import { List, Card, Row, Col } from "antd";
import FlightResultsContext from "../../contexts/FlightResultsContext";

const Results: React.FC = () => {
    const { flightResults } = useContext(FlightResultsContext);

    console.log(flightResults);
    return (
        <List
            dataSource={flightResults.data}
            renderItem={(offer: any) => (
                <List.Item>
                    <Card>
                        <Row>
                            {offer.itineraries.map((itinerary: any) => (
                                <React.Fragment key={itinerary.id}>
                                    <Row>
                                        <Col>
                                            <p>Total time: {itinerary.duration}</p>
                                            {itinerary.segments.map((segment: any) => (
                                                <Card.Grid hoverable={false} key={segment.id}>
                                                    <Row>
                                                        <p>{segment.departure.at} - {segment.arrival.at}</p>
                                                    </Row>
                                                    <Row>
                                                        <Col><p>{segment.departure.iataCode} - {segment.arrival.iataCode}</p></Col>
                                                        <Col><p>{segment.duration}</p></Col>
                                                    </Row>
                                                </Card.Grid>
                                            ))}
                                        </Col>
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
                        </Row>
                    </Card>
                </List.Item>
            )}
        />
    );
};

export default Results;
