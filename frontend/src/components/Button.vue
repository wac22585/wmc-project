<template>
    <div class="btn-div">
        <v-btn :type="type" class="submit-btn">{{ label }}</v-btn>
    </div>
</template>

<script>
export default {
    props: {
        type: {
            type: String,
            default: 'button',
            validator: value => ['button', 'submit'].includes(value)
        },
        label: {
            type: String,
            default: 'Continue'
        },
    }
};
</script>

<style scoped>
    .submit-btn {
        color: white;
        background: black;
        border-radius: 20px;
        font-size: 10px;
    }
</style>
