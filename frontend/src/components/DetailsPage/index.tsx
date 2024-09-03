import React, {useContext} from "react";
import FlightResultsContext from "../../contexts/FlightResultsContext";
import { Card, Col, List, Row } from "antd";

const DetailsPage: React.FC = () =>{
    const {selectedFlight} = useContext(FlightResultsContext);

    return(
        <div>
            <h1>Flights Details</h1>
            <Row>
                <Col>
                    <List
                        dataSource={selectedFlight.itineraries}
                        renderItem={(itinerary:any)=>(
                            <List.Item>
                                {itinerary.segments.map((segment:any)=>{
                                    const travelerFareDetails = selectedFlight.travelerPricings.flatMap((traveler: { fareDetailsBySegment: any; })=>traveler.fareDetailsBySegment)
                                    .find((fare: { segmentId: any; })=>fare.segmentId === segment.id);

                                    return(
                                        <Row>
                                            <Card>
                                                <Row>
                                                    <Col>
                                                        <p>Segment: {segment.id}</p> <br />
                                                        <p>{segment.departure.at} - {segment.arrival.at}</p> <br />
                                                        <p>{segment.departure.iataCode} - {segment.arrival.iataCode}</p> <br />
                                                        <p>{segment.carrierCode}</p> <br />
                                                        <p>Flight number: {segment.number}</p>
                                                    </Col>
                                                    <Col>
                                                        <p>Travelers fare details</p>
                                                        <p>Cabin: {travelerFareDetails.cabin}</p>
                                                        <p>Class: {travelerFareDetails.class}</p>
                                                    </Col>
                                                </Row>

                                            </Card>
                                        </Row>

                                    );

                                })}
                            </List.Item>
                        )}
                    >
                    </List>
                </Col>
                <Col>
                        <h3>Price Breakdown</h3>
                        <p>Base: $ {selectedFlight.price.base} {selectedFlight.price.currency}</p> <br />
                        <p>Fees</p> <br />
                        <ul>
                            {selectedFlight.price.fees.map((fee: any)=>(
                                <li> {fee.type}: ${fee.amount} {selectedFlight.price.currency} </li>
                            ))}
                        </ul>
                        <p>Total: $ {selectedFlight.price.total} {selectedFlight.price.currency}</p>
                        <p>Price per traveler</p>
                        <ul>
                            {selectedFlight.travelerPricings.map((price: any) => (
                                <li key={price.travelerId}>
                                    {price.travelerType} {price.travelerId}: $ {price.price.total} {price.price.currency}
                                </li>
                            ))}
                        </ul>
                </Col>
            </Row>
        </div>
    );
};

export default DetailsPage;