#version 330 core
out vec4 FragColor;
in vec2 ourPos;

void main()
{

    FragColor = vec4(ourPos + vec2(.5f), 0.2f, 1.0f);
}