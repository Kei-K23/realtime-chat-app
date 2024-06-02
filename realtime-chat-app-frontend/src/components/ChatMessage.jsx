import Avatar from "react-avatar";
import { Box } from "@mui/material";
import PropTypes from "prop-types";

function ChatMessage({ message, username }) {
  ChatMessage.propTypes = {
    username: PropTypes.string.isRequired,
    message: PropTypes.object.isRequired,
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: message.sender === username ? "flex-end" : "flex-start",
        margin: "10px 0",
      }}
    >
      <Box
        sx={{
          display: "flex",
          flexDirection: message.sender === username ? "row-reverse" : "row",
          alignItems: "center",
          gap: 1,
        }}
      >
        <Avatar name={message.sender} size="35" round={true} />
        <h4>{message.sender}</h4>
      </Box>
      <Box
        sx={{
          backgroundColor:
            message.sender === username ? "primary.main" : "secondary.main",
          color: "white",
          borderRadius: "12px",
          padding: "10px",
          maxWidth: "80%",
        }}
      >
        <p>{message.content}</p>
      </Box>
    </Box>
  );
}

export default ChatMessage;
