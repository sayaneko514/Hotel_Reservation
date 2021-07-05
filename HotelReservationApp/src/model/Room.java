package model;

public class Room implements IRoom
{
    private final String roomNumber;
    private final Double price;
    private final RoomType roomType;

    public Room (String roomNumber, Double price, RoomType roomType)
    {
        super();
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public String toString()
    {
        return ("Room#: "+ roomNumber + "  Room type: " + roomType + "  $"+ price +"/night");
    }

}
