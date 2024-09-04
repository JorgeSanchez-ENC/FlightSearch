import React, {useContext} from "react";
import FlightResultsContext from "../../contexts/FlightResultsContext";
import { Card, Col, Divider, Flex, List, Row, Space, Typography } from "antd";
import "./index.css";
import dayjs from "dayjs";

const { Title, Paragraph} = Typography;

const DetailsPage: React.FC = () =>{
    const {selectedFlight} = useContext(FlightResultsContext);

    return(

        <Flex vertical className="detailsFlex" >
            <div>
            <Row align={"middle"}>
                <Col span={24}>
                    <Title>Flight Details</Title>
                
                </Col>
            </Row>

            </div>
            
            <Row>
                <Col span={17}>
                    <List
                        itemLayout="vertical"
                        dataSource={selectedFlight.itineraries}
                        renderItem={(itinerary:any)=>(
                            <List.Item>
                                {itinerary.segments.map((segment:any)=>{
                                    const travelerFareDetails = selectedFlight.travelerPricings.flatMap((traveler: { fareDetailsBySegment: any; })=>traveler.fareDetailsBySegment)
                                    .find((fare: { segmentId: any; })=>fare.segmentId === segment.id);

                                    return(
                                        <Row style={{ marginBottom: '16px' }}>
                                            <Card style={{ width: '80%' }}>
                                                <Row>
                                                    <Col span={18}>
                                                        <Title level={4}>Segment: {segment.id}</Title> 
                                                        <Paragraph>{dayjs(segment.departure.at).format('YYYY-MM-DD HH:mm')} - {dayjs(segment.arrival.at).format('YYYY-MM-DD HH:mm')}</Paragraph>
                                                        <Paragraph>{segment.departure.iataCode} - {segment.arrival.iataCode}</Paragraph>
                                                        <Paragraph>{segment.carrierCode}</Paragraph>
                                                        <Paragraph>Flight number: {segment.number}</Paragraph>
                                                    </Col>
                                                    
                                                    <Col span={6}>
                                                        <Title level={4}>Travelers fare details</Title>
                                                        <Paragraph>Cabin: {travelerFareDetails.cabin}</Paragraph>
                                                        <Paragraph>Class: {travelerFareDetails.class}</Paragraph>
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
                <Divider type="vertical" style={{ height: '100%' }} />
                <Col span={6}>
                        <Title level={4}>Price Breakdown</Title>
                        <Paragraph>Base: $ {selectedFlight.price.base} {selectedFlight.price.currency}</Paragraph> 
                        <Paragraph>Fees</Paragraph> 
                        <ul>
                            {selectedFlight.price.fees.map((fee: any)=>(
                                <li> {fee.type}: ${fee.amount} {selectedFlight.price.currency} </li>
                            ))}
                        </ul>
                        <Paragraph>Total: $ {selectedFlight.price.total} {selectedFlight.price.currency}</Paragraph>
                        <Paragraph>Price per traveler</Paragraph>
                        <ul>
                            {selectedFlight.travelerPricings.map((price: any) => (
                                <li key={price.travelerId}>
                                    {price.travelerType} {price.travelerId}: $ {price.price.total} {price.price.currency}
                                </li>
                            ))}
                        </ul>
                </Col>
            </Row>
       </Flex>
        
    );
};

export default DetailsPage;