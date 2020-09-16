import React from 'react';
import CircularProgress from '@material-ui/core/CircularProgress';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';

export default function Progress(props) {
    const { value } = props;
    return (
        <Box position="relative" display="inline-flex">
            <CircularProgress variant="static" {...props} />
            <Box
                top={0}
                left={0}
                bottom={0}
                right={0}
                position="absolute"
                display="flex"
                alignItems="center"
                justifyContent="center"
            >
                {value && (
                    <Typography variant="caption" component="div" color="textSecondary">
                        {`${Math.round(value)}%`}
                    </Typography>
                )}
            </Box>
        </Box>
    );
}
