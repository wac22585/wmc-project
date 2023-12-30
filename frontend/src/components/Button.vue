<template>
    <div class="btn-div">
        <v-btn 
            :type="type" 
            :density="density"
            :class="buttonClasses"
        >{{ label }}</v-btn>
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
        density: {
            type: String,
            default: 'default'
        },
        useWhiteBackground: {
            type: Boolean,
            default: false
        }
    },
    computed: {
        buttonClasses() {
            return {
                'bg-white': this.useWhiteBackground,
                'submit-btn': true,
            }
        }
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

    .bg-white {
        background: white;
        color: black;
        border: 1px solid #707070;
        margin-right: 30px;
    }
</style>
